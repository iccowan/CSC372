# Ian Cowan
# CSC372 A5
# Bayes Net Inferencing

defmodule Bnet.Node do

  # Bayes Net Node Struct
  defstruct [
    name: nil,
    parents: [],
    children: [],
    truth_table: %{}
  ]

end
