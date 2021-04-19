# Ian Cowan
# CSC372 A5
# Bayes Net Inferencing

defmodule Bnet.Bnet do

  # Bayes Net Struct
  defstruct [
    roots: [ :B, :E, ],
    node_names: [ :E, :B, :A, :J, :M ],
    nodes: %{},
  ]

end
