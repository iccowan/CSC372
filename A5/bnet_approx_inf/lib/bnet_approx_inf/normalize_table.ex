# Ian Cowan
# CSC372 A5
# Bayes Net Inferencing

defmodule BnetApproxInf.NormalizeTable do

  # Normalize the table against the total number of iterations
  def normalize({ table, total_iterations }) do
    table
    |> normalize(Map.keys(table), total_iterations)
  end

  # Normalie each entry in the table
  def normalize(table, [], _iterations) do
    table
  end
  def normalize(table, [ first_entry | rest_of_entries ], iterations) do
    table
    |> Map.update(first_entry, 0.0, &( &1 / iterations ))
    |> normalize(rest_of_entries, iterations)
  end

end
