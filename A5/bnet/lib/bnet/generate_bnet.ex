defmodule Bnet.GenerateBnet do

  alias Bnet.{ Bnet, Node }

  def get_bnet() do
    # Hard code the BNET for this problem
    %Bnet{
      nodes: get_nodes()
    }
  end

  defp get_nodes() do
    %{
      "E" => get_e(),
      "B" => get_b(),
      "A" => get_a(),
      "J" => get_j(),
      "M" => get_m(),
    }
  end

  defp get_e() do
    %Node{
      name: "E",
      children: [ "A", ],
      truth_table: %{ { -1 } => 0.03 }
    }
  end

  defp get_b() do
    %Node{
      name: "B",
      children: [ "A", ],
      truth_table: %{ { -1 } => 0.02 }
    }
  end

  defp get_a() do
    %Node{
      name: "A",
      parents: [ "B", "E" ],
      children: [ "J", "M", ],
      truth_table: %{
        { 1, 1 } => 0.97,
        { 1, 0 } => 0.92,
        { 0, 1 } => 0.36,
        { 0, 0 } => 0.03,
      }
    }
  end

  defp get_j() do
    %Node{
      name: "J",
      parents: [ "A" ],
      children: [],
      truth_table: %{
        { 1 } => 0.85,
        { 0 } => 0.07,
      }
    }
  end

  defp get_m() do
    %Node{
      name: "M",
      parents: [ "A" ],
      children: [],
      truth_table: %{
        { 1 } => 0.69,
        { 0 } => 0.02,
      }
    }
  end

end
