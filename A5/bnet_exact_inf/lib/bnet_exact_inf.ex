# Ian Cowan
# CSC372 A5
# Bayes Net Inferencing

defmodule BnetExactInf do

  # Handles getting the exact probability based on the given query
  defdelegate get_prob(request),
    to: BnetExactInf.ExactInf

end
