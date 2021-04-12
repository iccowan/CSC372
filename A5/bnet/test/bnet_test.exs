defmodule BnetTest do
  use ExUnit.Case
  doctest Bnet

  test "greets the world" do
    assert Bnet.hello() == :world
  end
end
