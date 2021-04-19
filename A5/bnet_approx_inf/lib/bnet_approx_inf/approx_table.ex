# Ian Cowan
# CSC372 A5
# Bayes Net Inferencing

defmodule BnetApproxInf.ApproxTable do

  # Generates the approximate table based off the Bayes Net and number
  # of iterations
  def get_approx_table({ bnet, iterations }) do
    { run_generate(bnet, iterations), iterations }
  end

  # Run through the generation for the number of iterations given
  defp run_generate(_bnet, 0) do
    # Finished
    %{}
  end
  defp run_generate(bnet, iterations) do
    # Add one to the table entry that gets randomly generated
    %{ generate_table(bnet, bnet.roots, [], %{}) => 1 }
    |> Map.merge(run_generate(bnet, iterations - 1), fn _k, v1, v2 ->
      v1 + v2
    end)
  end

  # Generate the table
  defp generate_table(_bnet, [], [], model) do
    # Finished
    model
  end
  defp generate_table(bnet, [], children, model) do
    # No more parents, onto children
    generate_table(bnet, children, [], model)
  end
  defp generate_table(bnet, [ first_root | rest_of_roots ], children, model) do
    # Add the node's children to the list of children
    # and make sure each child is added exactly once
    children = children ++ Map.get(bnet.nodes, first_root).children
               |> Enum.uniq()

    # Get a random value and put it in the map
    rand_val = gen_val(first_root, bnet, model)
    model = Map.put_new(model, first_root, rand_val)

    # Recurse on the rest of the parents
    generate_table(bnet, rest_of_roots, children, model)
  end

  # Generate a value randomly
  defp gen_val(node, bnet, model) do
    # Get the node and the node's truth table for the randomness by probability
    node = Map.get(bnet.nodes, node)
    Map.get(node.truth_table, parent_truth_val(node.parents, model))
    |> rand_true_or_false(:rand.uniform())
  end

  # Finds the trutb value based on the model
  defp parent_truth_val([], _model) do
    # Finished
    %{}
  end
  defp parent_truth_val([ parent | rest_of_parents ], model) do
    # Gets the value of the parent and adds it to the model
    %{ parent => Map.get(model, parent) }
    |> Map.merge(parent_truth_val(rest_of_parents, model))
  end

  # Randomly generate a truth value based on a random float 0 < r < 1
  defp rand_true_or_false(p_true, rand_num) when rand_num < p_true,
    do: 1
  defp rand_true_or_false(p_true, rand_num) when rand_num >= p_true,
    do: 0

end
