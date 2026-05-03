{
  description = "Miencraft Challenges Mod";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-25.11";
    systems.url = "github:nix-systems/default";
    git-hooks = {
      url = "github:cachix/git-hooks.nix";
      inputs.nixpkgs.follows = "nixpkgs";
    };
  };

  outputs =
    {
      self,
      nixpkgs,
      systems,
      git-hooks,
    }:
    let
      forEachSystem = nixpkgs.lib.genAttrs (import systems);
    in
    {
      # Run the hooks with `nix fmt`.
      formatter = forEachSystem (
        system:
        let
          pkgs = nixpkgs.legacyPackages.${system};
          config = self.checks.${system}.pre-commit-check.config;
          inherit (config) package configFile;
          script = ''
            ${pkgs.lib.getExe package} run --all-files --config ${configFile}
          '';
        in
        pkgs.writeShellScriptBin "pre-commit-run" script
      );

      # Run the hooks in a sandbox with `nix flake check`.
      # Read-only filesystem and no internet access.
      checks = forEachSystem (
        system:
        let
          pkgs = nixpkgs.legacyPackages.${system};
        in
        {
          pre-commit-check = git-hooks.lib.${system}.run {
            src = ./.;
            hooks = {
              # Google Java Format — opinionated, no-config formatter
              google-java-format = {
                enable = true;
                # Format all Java sources, including generated ones under src/
                files = "\\.(java)$";
                entry = "${pkgs.google-java-format}/bin/google-java-format --replace";
              };

              # Keep Gradle wrapper scripts tidy
              end-of-file-fixer.enable = true;
              # trailing-whitespace.enable = true;
              mixed-line-endings.enable = true;
              check-json.enable = true;
              nixfmt.enable = true;
            };
          };
        }
      );

      # Enter a development shell with `nix develop`.
      # The hooks will be installed automatically.
      # Or run pre-commit manually with `nix develop -c pre-commit run --all-files`
      devShells = forEachSystem (system: {
        default =
          let
            pkgs = nixpkgs.legacyPackages.${system};
            inherit (self.checks.${system}.pre-commit-check) shellHook enabledPackages;
          in
          pkgs.mkShell {
            inherit shellHook;
            buildInputs = enabledPackages;
          };
      });
    };
}
