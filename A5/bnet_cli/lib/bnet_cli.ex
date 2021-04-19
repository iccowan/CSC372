# Ian Cowan
# CSC372 A5
# Bayes Net Inferencing

# In order to compile this program, ensure that you are
# in the CLI directory and run 'mix escript.build'. There will be a bin file
# 'bnet' generated. In order to run the program, run it with './bnet [query]'
# where [query] is a query given in the format below.
#
# For the query, list the node names with their approrpiate values for
# inferencing. If you have given values, separate these with the keyword
# 'given'.
#
# Example Queries...
#   P(A | B) is represented by './bnet At given B'
#   P(A | ~B) is represented by './bnet At given Bf'
#   P(A, B) is represented by './bnet At Bt'
#   P(A, ~B) is represented by './bnet At Bf'
#   P(A, ~B | D, E) is represented by './bnet At Bf given Dt Et'

defmodule BnetCli do

  # Main
  def main(args) do
    process_input({ [], [] }, false, args)
    |> solve()
  end


  # Solves the given problem based on the 'vals' and 'given'.
  # Will output the result to the console
  defp solve({ vals, given }) do
    # Handles the exact inference solving
    IO.write("Exact Inference Calculation: ")
    BnetExactInf.get_prob(%{ vals: vals, given: given })
    |> IO.puts

    # Handles the approximate inference solving
    IO.write("Approximate Inference Calculation: ")
    BnetApproxInf.get_prob(%{ vals: vals, given: given })
    |> IO.puts
  end


  # Processes the input from inputs and converts to something we can work with
  defp process_input(inputs, _given, [ h | t ])
    when h == "given" do
    # If we're switching to given, make it known
    process_input(inputs, true, t)
  end
  defp process_input({ vals, given }, _given = true, [ h ]) do
    # Process the inputs for given and finish...the list is empty
    { vals, process(given, h) }
  end
  defp process_input({ vals, given }, _given = true, [ h | t ]) do
    # Process the inputs for the given and the recurse on the tail
    { vals, process(given, h) }
    |> process_input(true, t)
  end
  defp process_input({ vals, given }, _given = false, [ h ]) do
    # Process the inputs for the vals...not at given yet
    # The list is empty so we terminate after this processing
    { process(vals, h), given }
  end
  defp process_input({ vals, given }, _given = false, [ h | t ]) do
    # Process the inputs for the vals...not a given yet
    # Then recurse on the rest of the list
    { process(vals, h), given }
    |> process_input(false, t)
  end

  # Process the given input and place in the list
  defp process(l, input) do
    # Get the codepoints since everything should be in the form 'Nv' Where
    # N is the node letter and v is the value ('t' or 'f')
    String.codepoints(input)

    # Get the node and values
    |> node_and_val()

    # Concat to the list and returns the list
    |> concat_list(l)
  end

  # Adds the given value, 'val', to the given 'list'
  defp concat_list(val, list) do
    list ++ List.wrap(val)
  end

  # Takes the node name if 't' -> true and returns the applicable Tuple
  defp node_and_val([ node, "t" ]) do
    # Make the node an atom and its value is 1
    { node |> String.to_atom(), 1 }
  end

  # Take the node name and if 'f' -> false and returns the applicable Tuple
  defp node_and_val([ node, "f" ]) do
    # Make the node an atom and its value is 0
    { node |> String.to_atom(), 0 }
  end

end
