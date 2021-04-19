# Ian Cowan
# CSC372 A5
# Bayes Net Inferencing

defmodule BnetApproxInf.ApproxInf do

  alias BnetApproxInf.{ ApproxTable, NormalizeTable, Lookup }

  # Gets the probability based on the values and given
  def get_prob(_request = %{ vals: vals, given: [] }, table) do
    # Lookup in the table and round to 6 decimal places
    # The table is given, no need to get one
    table
    |> Lookup.lookup(vals)
    |> Float.round(6)
  end
  def get_prob(_request = %{ vals: vals, given: [] }) do
    # Lookup in the table and round to 6 decimal places
    # No table given, get one
    get_table()
    |> Lookup.lookup(vals)
    |> Float.round(6)
  end
  def get_prob(_request = %{ vals: vals, given: given }) do
    # When something is given, simply calculate the P(vals U given) / P(given)
    # and round to 6 decimal places
    table = get_table()
    get_prob(%{ vals: vals ++ given, given: [] }, table) /
      get_prob(%{ vals: given, given: [] }, table)
    |> Float.round(6)
  end

  # Generates the table
  defp get_table() do
    # 100,000,000 iterations
    { Bnet.get(), 100000000 }
    |> ApproxTable.get_approx_table()

    # Normalize from numbers into fractions for probabilities
    |> NormalizeTable.normalize()
  end

end
