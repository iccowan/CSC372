defmodule BnetCli.MixProject do
  use Mix.Project

  def project do
    [
      app: :bnet_cli,
      version: "0.1.0",
      elixir: "~> 1.11",
      start_permanent: Mix.env() == :prod,
      deps: deps(),

      escript: [ main_module: BnetCli, name: "bnet" ],
    ]
  end

  # Run "mix help compile.app" to learn about applications.
  def application do
    [
      extra_applications: [:logger]
    ]
  end

  # Run "mix help deps" to learn about dependencies.
  defp deps do
    [
      # {:dep_from_hexpm, "~> 0.3.0"},
      # {:dep_from_git, git: "https://github.com/elixir-lang/my_dep.git", tag: "0.1.0"}
      { :bnet, path: "../bnet/" },
      { :bnet_exact_inf, path: "../bnet_exact_inf/" },
      { :bnet_approx_inf, path: "../bnet_approx_inf/" },
    ]
  end
end
