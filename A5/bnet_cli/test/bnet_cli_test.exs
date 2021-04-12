defmodule BnetCliTest do
  use ExUnit.Case
  doctest BnetCli

  test "greets the world" do
    assert BnetCli.hello() == :world
  end
end
