defmodule BnetExactInf do

  defdelegate get_prob(request),
    to: BnetExactInf.ExactInf

end
