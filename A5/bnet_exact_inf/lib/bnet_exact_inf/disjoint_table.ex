# Ian Cowan
# CSC372 A5
# Bayes Net Inferencing

defmodule BnetExactInf.DisjointTable do

  alias BnetExactInf.DisjointTable

  # Disjoint table struct
  # This turns out to not be necessary, but the code was written to
  # use the struct and it doesn't affect the space nearly at all
  # for this small problem
  defstruct [
    nodes: [],
    table: %{},
  ]

  # Generates the disjoint table for the 'bnet'
  def get_disjoint_table(bnet) do
    # Count the total number of nodes in the net
    Enum.count(bnet.nodes)

    # Generate a list for the number of nodes of all 0's
    |> generate_list()

    # Generate all of the binary combinations of that list of 0's
    |> all_bin_combos()

    # Generate the table based on the binary combinations from the net
    |> generate_table(bnet)
  end

  # Generate the list of 0's based on a passed length
  def generate_list(0) do
    # Finished
    []
  end
  def generate_list(num) do
    # Add a zero and recurse
    [ 0 ] ++ generate_list(num - 1)
  end

  # Takes a base list of all 0's, 'init_list' and returns a list of list of
  # all the binary combinations of that length
  defp all_bin_combos(init_list) do
    # Helper, call actual function
    all_bin_combos(Enum.sum(init_list), Enum.count(init_list),
      init_list, [ init_list ])
  end
  defp all_bin_combos(sum_and_total, sum_and_total, _last_list, combos) do
    # Finished
    combos
  end
  defp all_bin_combos(_sum, _total, last_list, combos) do
    # Generate the next binary combination (step from the last) and recurse
    new_list = step_bin(last_list)
    all_bin_combos(Enum.sum(new_list), Enum.count(new_list),
      new_list, combos ++ [ new_list ])
  end

  # Steps from the last binary combination to the next in sequence
  defp step_bin([]) do
    # Finished
    []
  end
  defp step_bin([ h | t ]) when rem(h, 2) == 0 do
    # Put a 1 in this place
    [ 1 ] ++ t
  end
  defp step_bin([ h | t ]) when rem(h, 2) == 1 do
    # Put a 0 in this place and recurse
    [ 0 ] ++ step_bin(t)
  end

  # Generates the disjoint table for each 'combos' and for the 'bnet'
  defp generate_table(combos, bnet) do
    # Helper, call actual function
    generate_table(%DisjointTable{}, combos, bnet)
  end
  defp generate_table(table, [], _bnet) do
    # Finished
    table
  end
  defp generate_table(table, [ first_combo | rest_combos ], bnet) do
    # Update the table with the new values
    %DisjointTable{ table |
      table:
        Map.put_new(table.table,
          # Zip together the node names and the combo for easy access later
          Enum.zip(bnet.node_names, first_combo) |> Map.new(),
          # Get the probability off the net
          prob(first_combo, bnet)
        )
    }
    # Recurse
    |> generate_table(rest_combos, bnet)
  end

  # Calculate the probability for a model from the net
  defp prob(model, bnet) do
    # Zip together the node names and model
    Enum.zip(bnet.node_names, model)

    # Accumulate the probability from the net
    |> accum_prob(bnet, bnet.node_names)
  end

  # Accumulates the probabilty for a given 'bnet' and 'model'
  defp accum_prob(_model, _bnet, []) do
    # Finished
    1.0
  end
  defp accum_prob(model, bnet, [ first_node | rest_of_roots ]) do
    # Get the probability for the node and multiply it by the probability of
    # all the other nodes according to this model
    get_node_prob(model, bnet, first_node) *
      accum_prob(model, bnet, rest_of_roots)
  end

  # Gets the probabily of a 'node' based on the given 'bnet' and 'model'
  defp get_node_prob(model, bnet, node) do
    # Get the node from the bnet
    node = bnet.nodes |> Map.get(node)

    # Get the node's truth table
    truth_table = node.truth_table

    # Get the truth values from the parents
    parent_truths = get_node_parent_truths(model, node.parents)

    # Calculate the probability
    prob(truth_table, parent_truths, List.keyfind(model, node.name, 0)
                                     |> elem(1))
  end

  # Get the truth model from the parents of a node
  defp get_node_parent_truths(_model, []) do
    # Finished
    %{}
  end
  defp get_node_parent_truths(model, [ first_parent | rest_of_parents ]) do
    # Set the first parent to the value of that parent in the model
    %{ first_parent => List.keyfind(model, first_parent, 0) |> elem(1) }

    # Merge with the rest of the parents
    |> Map.merge(get_node_parent_truths(model, rest_of_parents))
  end

  # Get the probability of a parent depending on true or false
  defp prob(truth_table, parent_truths, _true_or_false = 1) do
    # Get the probability
    Map.get(truth_table, parent_truths)
  end
  defp prob(truth_table, parent_truths, _true_or_false = 0) do
    # False, so 1 - probability
    1.0 - prob(truth_table, parent_truths, 1)
  end

end
