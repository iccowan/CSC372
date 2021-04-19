# Ian Cowan
# CSC372 A5
# Bayes Net Inferencing

defmodule BnetApproxInf.Lookup do

  # Looks up the table for the given vals
  def lookup(table, vals) do
    vals
    |> Map.new()
    |> prob(table)
  end

  # Filters out the table against the model
  defp prob(model, table) do
    table
    |> Enum.filter(fn { k, _v } ->
      sub_map = Map.split(k, Map.keys(model))
                |> elem(0)

      match?(^model, sub_map)
    end)

    # Add up the probabilities
    |> Enum.reduce(0, fn { _k, v }, acc ->
      acc + v
    end)
  end

end
