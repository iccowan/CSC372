defmodule BnetExactInf.DisjointTable do

  alias BnetExactInf.DisjointTable

  defstruct [
    nodes: [],
    table: %{},
  ]

  def get_disjoint_table(bnet) do
    Enum.count(bnet.nodes)
    |> generate_list()
    |> all_bin_combos()
    |> generate_table(bnet)
  end

  defp sum(%DisjointTable{ table: table }) do
    Map.to_list(table)
    |> sum()
  end

  defp sum([]) do
    0
  end

  defp sum([ entry1 | entries ]) do
    elem(entry1, 1) + sum(entries)
  end

  def generate_list(0) do
    []
  end

  def generate_list(num) do
    [ 0 ] ++ generate_list(num - 1)
  end

  defp all_bin_combos(init_list) do
    all_bin_combos(init_list, [ init_list ])
  end

  defp all_bin_combos(last_list, combos) do
    if Enum.sum(last_list) != Enum.count(last_list) do
      new_list = step_bin(last_list)
      all_bin_combos(new_list, combos ++ [ new_list ])
    else
      combos
    end
  end

  defp step_bin([]) do
    []
  end

  defp step_bin([ h | t ]) when rem(h, 2) == 0 do
    [ 1 ] ++ t
  end

  defp step_bin([ h | t ]) when rem(h, 2) == 1 do
    [ 0 ] ++ step_bin(t)
  end

  defp generate_table(combos, bnet) do
    generate_table(%DisjointTable{}, combos, bnet)
  end

  defp generate_table(table, [], bnet) do
    table
  end

  defp generate_table(table, [ first_combo | rest_combos ], bnet) do
    %DisjointTable{ table |
      table:
        Map.put_new(table.table,
          Enum.zip(bnet.node_names, first_combo) |> Map.new(),
          prob(first_combo, bnet)
        )
    }
    |> generate_table(rest_combos, bnet)
  end

  defp prob(model, bnet) do
    Enum.zip(bnet.node_names, model)
    |> accum_prob(bnet, bnet.node_names)
  end

  defp accum_prob(model, bnet, []) do
    1.0
  end

  defp accum_prob(model, bnet, [ first_node | rest_of_roots ]) do
    get_node_prob(model, bnet, first_node) * accum_prob(model, bnet, rest_of_roots)
  end

  defp get_node_prob(model, bnet, node) do
    node = bnet.nodes |> Map.get(node)
    truth_table = node.truth_table
    parent_truths = get_node_parent_truths(model, node.parents)

    prob(truth_table, parent_truths, List.keyfind(model, node.name, 0) |> elem(1))
  end

  defp get_node_parent_truths(model, []) do
    %{}
  end

  defp get_node_parent_truths(model, [ first_parent | rest_of_parents ]) do
    %{ first_parent => List.keyfind(model, first_parent, 0) |> elem(1) }
    |> Map.merge(get_node_parent_truths(model, rest_of_parents))
  end

  defp prob(truth_table, parent_truths, _true_or_false = 1) do
    Map.get(truth_table, parent_truths)
  end

  defp prob(truth_table, parent_truths, _true_or_false = 0) do
    1.0 - prob(truth_table, parent_truths, 1)
  end

end
