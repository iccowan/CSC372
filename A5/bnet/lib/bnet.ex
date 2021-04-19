# Ian Cowan
# CSC372 A5
# Bayes Net Inferencing

defmodule Bnet do

  alias Bnet.GenerateBnet

  # Retrieve a generated Bayes Net
  defdelegate get(),
    to: GenerateBnet

end
