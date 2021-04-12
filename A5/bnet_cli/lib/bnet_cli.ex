defmodule BnetCli do

  def main(args) do
    process_input({ [], [] }, false, args)
    |> solve()
  end

  defp solve({ vals, given }) do
    # TODO: We need to call the appropriate solver here...
    IO.inspect vals
    IO.inspect given
    0
  end

  defp process_input(inputs, _given, [ h | t ])
    when h == "given" do
    process_input(inputs, true, t)
  end

  defp process_input({ vals, given }, _given = true, [ h ]) do
    { vals, process(given, h) }
  end

  defp process_input({ vals, given }, _given = true, [ h | t ]) do
    { vals, process(given, h) }
    |> process_input(true, t)
  end

  defp process_input({ vals, given }, _given = false, [ h ]) do
    { process(vals, h), given }
  end

  defp process_input({ vals, given }, _given = false, [ h | t ]) do
    { process(vals, h), given }
    |> process_input(false, t)
  end

  defp process(l, input) do
    String.codepoints(input)
    |> node_and_val()
    |> concat_list(l)
  end

  defp concat_list(val, list) do
    list ++ List.wrap(val)
  end

  defp node_and_val([ node, "t" ]) do
    { node, 1 }
  end

  defp node_and_val([ node, "f" ]) do
    { node, 0 }
  end

end
