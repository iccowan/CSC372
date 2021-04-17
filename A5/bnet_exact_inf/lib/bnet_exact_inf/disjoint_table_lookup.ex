defmodule BnetExactInf.DisjointTableLookup do

  alias BnetExactInf.DisjointTable

  def lookup(disjoint_table, vals) do
    vals
    |> Map.new()
    |> prob(disjoint_table)
  end

  defp prob(model, disjoint_table) do
    disjoint_table.table
    |> Enum.filter(fn { k, _v } ->
      sub_map = Map.split(k, Map.keys(model))
                |> elem(0)

      match?(^model, sub_map)
    end)
    |> Enum.reduce(0, fn { _k, v }, acc ->
      acc + v
    end)
  end

end
