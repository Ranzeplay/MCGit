# MCGit

A backup plugin for Minecraft Spigot server

*Build Status:* [![Build status](https://dev.azure.com/ranzeplay/MCGit/_apis/build/status/MCGit-Maven-CI)](https://dev.azure.com/ranzeplay/MCGit/_build/latest?definitionId=-1)

---

### Commands (with command completer):

- **Commit**

    - **/mcgit archive <description>** to make a new archive and pack current game save into a zip file to backup directory

    - **/mcgit view <archives|archive [archive id]|collections|collection [collection id]>** to view details of collection(s) or archive(s)

    - **/mcgit rollback <archive id>** to rollback server save to a specific version of your server save, changes will be applied on the next server startup

    - **/mcgit delete <archive id>** to delete a specific archive forever

- **Collection**

    - **/mcgit collection add <collectionName> <collectionDescription>** to create a archives collection to collate archives

    - **/mcgit collection delete <collectionId>** to delete a collection

    - **/mcgit collection addCommit <collectionId> <commitId>** to add a archive into a collection

    - **/mcgit collection removeCommit <collectionId> <commitId>** to remove a archive from a collection

- **/mcgit gui** to open a GUI contains all your up to 54 archives

- **/mcgit help** to view command help

### Dependencies:

None

### How to use:
Just put it to your plugins directory

### Q&A:

**Q:** How do I read backups, can it only be used in MCGit?

**A:** No, the backup will be saved as zip format, you can download or do anything with it directly.

---

### Links:
- Spigotmc: [Plugin Overview](https://www.spigotmc.org/resources/mcgit.78677)