# Ian Cowan
# CSC372 A5
# Bayes Net Inferencing

defmodule BnetExactInf.DisjointTableLookup do

  # Looks up the vals in the disjoint table
  def lookup(disjoint_table, vals) do
    vals
    |> Map.new()
    |> prob(disjoint_table)
  end

  # Calculates the proba for the model in the table
  defp prob(model, disjoint_table) do
    # Takes the table and filters against the given model
    disjoint_table.table
    |> Enum.filter(fn { k, _v } ->
      sub_map = Map.split(k, Map.keys(model))
                |> elem(0)

      match?(^model, sub_map)
    end)

    # Sum up all of the entries that have been filtered through
    |> Enum.reduce(0, fn { _k, v }, acc ->
      acc + v
    end)
  end

end
