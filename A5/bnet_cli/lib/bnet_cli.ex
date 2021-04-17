defmodule BnetCli do

  def main(args) do
    process_input({ [], [] }, false, args)
    |> solve()
  end

  defp solve({ vals, given }) do
    IO.write("Exact Inference Calculation: ")
    BnetExactInf.get_prob(%{ vals: vals, given: given })
    |> IO.puts

    IO.write("Approximate Inference Calculation: ")
    # TODO
    #|> BnetApproxInf.get_prob(%{ vals: vals, given, given })
    "To be implemented..."
    |> IO.puts
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
    { node |> String.to_atom(), 1 }
  end

  defp node_and_val([ node, "f" ]) do
    { node |> String.to_atom(), 0 }
  end

end