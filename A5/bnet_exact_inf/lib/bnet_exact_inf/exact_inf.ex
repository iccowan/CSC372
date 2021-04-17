defmodule BnetExactInf.ExactInf do

  alias BnetExactInf.{ DisjointTable, DisjointTableLookup }

  def get_prob(request = %{ vals: vals, given: [] }) do
    Bnet.get()
    |> DisjointTable.get_disjoint_table()
    |> DisjointTableLookup.lookup(vals)
    |> Float.round(6)
  end

  def get_prob(request = %{ vals: vals, given: given }) do
    get_prob(%{ vals: vals ++ given, given: [] }) / get_prob(%{ vals: given, given: [] })
    |> Float.round(6)
  end

end
