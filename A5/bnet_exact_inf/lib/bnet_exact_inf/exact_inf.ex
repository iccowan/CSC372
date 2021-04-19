# Ian Cowan
# CSC372 A5
# Bayes Net Inferencing

defmodule BnetExactInf.ExactInf do

  alias BnetExactInf.{ DisjointTable, DisjointTableLookup }

  # Gets the probability based on the vals and given
  def get_prob(_request = %{ vals: vals, given: [] }, table) do
    # Table is given, no need to generate
    # Lookup prob and round to 6 decimal places
    table
    |> DisjointTableLookup.lookup(vals)
    |> Float.round(6)
  end
  def get_prob(_request = %{ vals: vals, given: [] }) do
    # No table given, generate
    # Lookup prob and round to 6 decimal places
    get_table()
    |> DisjointTableLookup.lookup(vals)
    |> Float.round(6)
  end
  def get_prob(_request = %{ vals: vals, given: given }) do
    # In order to calculate the given, simply calculate
    # P(vals ++ given) / P(given)
    table = get_table()
    get_prob(%{ vals: vals ++ given, given: [] }, table) /
    get_prob(%{ vals: given, given: [] }, table)
    |> Float.round(6)
  end

  # Gets the disjoint table
  defp get_table() do
    # Get the Bayes net and generate the table
    Bnet.get()
    |> DisjointTable.get_disjoint_table()
  end

end
