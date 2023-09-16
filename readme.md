# Core plugin for InfiniteMinecrafters server

## Making changes
### Commits

Commit messages should be structured like this:


```
<type>[optional scope]: <description>
# OR, FOR BREAKING CHANGES
<type>[optional scope]!: <description>

[optional body]

[optional footer(s)]
```

...where `type` can be any of the following:

```
feat: Features
fix: Bug fixes
docs: Documentation
style: Styles
refactor: Code Refactoring
perf: Performance Improvements
test: Tests
build: Build changes
ci: CI Config
chore: Chores (other changes)
revert: Revert a commit
```

### Examples
```
fix(modules): load crash due to thread unsafety
```

```
build: change maven artifactId
```

## Modules
- chat
- chat games
- discord
- homes
- inventory
- rtp
- spawn
- staff
- warps

## Permission
Per module permission:
For each the modules' player commands, their base usage permission will be `coreplugin.<module-id>.command.player.<command-name>.use`.
Each player command's sub permission will look like this: `coreplugin.<module-id>.command.player.<command-name>.<sub-branch>`.

## Data Storage
Data storage modules for each module are stored at the location of `Main.getInstance().getDataFolder() + "/data/modules/<module-id>"`. 
Data for users are stored at `Main.getInstance.getDataFolder + "data/users"`

## Per Module Todo List

#### Home Module
Todo list:
- Add appropriate permissions for the `home` command
- Add tab completion for the `home` command
- Add a `homeadmin` command class
- Add a `homeadmin create <player> <home name>` subcommand
- Add a `homeadmin delete <player> <hoem name>` subcommand
- Add a `homeadmin view <player>` subcommand
- Add a `homeadmin tpto <player> <home name>` subcommand
- Ability to block players doing `home` command in certain worlds

#### Warps Module
Todo list:
- Add a `warps` command class
- Add gui handling for `warps` command
- Add a `warp` command class
- Add a `warp <warp name>` subcommand
- Add a `warp create <name>` subcommand
- Add a `warp delete <name>` subcommand
- Ability to block players doing `warp` or `warps` command in certain worlds


### Spawn Module
Todo list:
- Add a `spawn <player>` subcommand
- Add a `setspawn` command class
- Add tab completion for `spawn` command
- Add appropriate permissions for `spawn` command
- Add a `spawn set` subcommand
- Ability to block `spawn` command in certain worlds