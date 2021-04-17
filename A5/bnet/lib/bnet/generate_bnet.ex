defmodule Bnet.GenerateBnet do

  alias Bnet.{ Bnet, Node }

  def get() do
    # Hard code the BNET for this problem
    %Bnet{
      nodes: get_nodes()
    }
  end

  defp get_nodes() do
    %{
      :E => get_e(),
      :B => get_b(),
      :A => get_a(),
      :J => get_j(),
      :M => get_m(),
    }
  end

  defp get_e() do
    %Node{
      name: :E,
      children: [ :A ],
      truth_table: %{ %{} => 0.03 }
    }
  end

  defp get_b() do
    %Node{
      name: :B,
      children: [ :A ],
      truth_table: %{ %{} => 0.02 }
    }
  end

  defp get_a() do
    %Node{
      name: :A,
      parents: [ :B, :E ],
      children: [ :J, :M ],
      truth_table: %{
        %{ :B => 1, :E => 1 } => 0.97,
        %{ :B => 1, :E => 0 } => 0.92,
        %{ :B => 0, :E => 1 } => 0.36,
        %{ :B => 0, :E => 0 } => 0.03,
      }
    }
  end

  defp get_j() do
    %Node{
      name: :J,
      parents: [ :A ],
      children: [],
      truth_table: %{
        %{ :A => 1 } => 0.85,
        %{ :A => 0 } => 0.07,
      }
    }
  end

  defp get_m() do
    %Node{
      name: :M,
      parents: [ :A ],
      children: [],
      truth_table: %{
        %{ :A => 1 } => 0.69,
        %{ :A => 0 } => 0.02,
      }
    }
  end

end
