defmodule Bnet do

  alias Bnet.GenerateBnet

  defdelegate get_bnet(),
    to: GenerateBnet

end
