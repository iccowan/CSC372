Bayes Net A5
============
Ian Cowan

Written in Elixir v1.11.4 (Compiled with Erlang OTP 23)

In order to run this program from binary, you must have at least Erlang OTP 23 installed. It is also recommended to have Elixir v1.11.4 installed, but that shoud not be required. Installation instructions for Erlang and Elixir are below.

- Erlang OTP 23 MacOS: Using brew, simply use the command `brew install erlang`
- Erlang OTP 23 Windows: Download the installer here [https://www.erlang.org/downloads](https://www.erlang.org/downloads)
- Erlang OTP 23 Build and Install: [https://erlang.org/doc/installation_guide/INSTALL.html](https://erlang.org/doc/installation_guide/INSTALL.html)
- Elixir 1.11.4: [https://elixir-lang.org/install.html](https://elixir-lang.org/install.html)

---

In order to compile and run the program, run the following commands from the base directory:

1. `cd bnet_cli`
2. `mix deps.get`
3. `mix escript.build`

These commands will get the dependencies and build the program. Please note that the binary file will be located in the `bnet_cli` directory after building. In order to run the program, use the following command:

- `./bnet [query]`

The query should be formatted as in the assignment. Some examples are given below of queries and their "written" counterparts.

*Examples:*
- P(A) = `./bnet At`
- P(A | B) = `./bnet At given Bt`
- P(A, B) = `./bnet At Bt`
- P(~A, B | ~C, D, E) = `./bnet Af Bt given Cf Dt Et`
