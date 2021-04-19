# Ian Cowan
# CSC372 A5
# Bayes Net Inferencing

defmodule BnetApproxInf do

  # Handles calculating the probability for the approximate inference
  defdelegate get_prob(request),
    to: BnetApproxInf.ApproxInf

end
