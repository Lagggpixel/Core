# Core plugin for Infinite Minecrafters server

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
- bazaar
- chat
- discord
- economy
- guild
- homes
- inventory
- restart
- rtp
- skipnight
- spawn
- staff
- warp

## Permission
Per module permission:
For each the modules' player commands, their base usage permission will be `coreplugin.<module-id>.command.player.<command-name>.use`.
Each player command's sub permission will look like this: `coreplug![img.png](img.png)in.<module-id>.command.player.<command-name>.<sub-branch>`.

## Data Storage
Data storage modules for each module are stored at the location of `Main.getInstance().getDataFolder() + "/data/modules/<module-id>"`. 
Data for users are stored at `Main.getInstance.getDataFolder + "data/users"`


## Per Module Todo List 
### Note this omits all the item marked in the code

**Bazaar Module**
- Fix items.json


**Chat Module**
- None

  
**Discord Module**
- None


**Economy Module**
- None


**Guild Module**
- None


**Home Module**
- None


**Inventory Module**
- TBC


**Restart Module**
- Add restart handling


**Rtp Module**
- Fix RtpCommand.java


**Skipnight Module**
- None


**Spawn Module**
- Ability to block `spawn` command in certain worlds


**Staff Module**
- Add staff mode
- Add staff action messages
- Add `broadcast`


**Warps Module**
- Add a `warps` command class
- Add bazaarGui handling for `warps` command
- Add a `warp` command class
- Add a `warp <warp name>` subcommand
- Add a `warp create <name>` subcommand
- Add a `warp delete <name>` subcommand
- Ability to block players doing `warp` or `warps` command in certain worlds
