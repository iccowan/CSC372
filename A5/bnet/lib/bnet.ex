defmodule Bnet do

  alias Bnet.{ DisjointTable, GenerateBnet }

  defdelegate get(),
    to: GenerateBnet

  defdelegate get_disjoint_table(bnet),
    to: DisjointTable

end
