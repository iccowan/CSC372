# Ian Cowan
# CSC372 A5
# Bayes Net Inferencing

defmodule Bnet.GenerateBnet do

  alias Bnet.{ Bnet, Node }

  # Generates the hard coded Bayes Net and returns a 'Bnet' struct
  def get() do
    # Hard code the BNET for this problem
    %Bnet{
      nodes: get_nodes()
    }
  end

  # Generates the nodes for the Bayes Net returning a 'Map' of nodes
  defp get_nodes() do
    # Generate the nodes
    %{
      :E => get_e(),
      :B => get_b(),
      :A => get_a(),
      :J => get_j(),
      :M => get_m(),
    }
  end

  # Returns a 'Node' representing the E node
  defp get_e() do
    %Node{
      name: :E,
      children: [ :A ],
      truth_table: %{ %{} => 0.03 }
    }
  end

  # Returns a 'Node' representing the B node
  defp get_b() do
    %Node{
      name: :B,
      children: [ :A ],
      truth_table: %{ %{} => 0.02 }
    }
  end

  #Returns a 'Node' representing the A node
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

  # Returns a 'Node' representing the J node
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

  # Returns a 'Node' representing the M node
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
