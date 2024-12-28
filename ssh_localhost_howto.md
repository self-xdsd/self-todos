In order to connect to localhost via SSH, you will need to generate a pair of public/private SSH keys. 
To generate a pair of keys and register them with your local SSH server, follow these steps:

https://docs.github.com/en/github/authenticating-to-github/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent

I suggest choosing a different name than ``id_ed25519``. You can leave the passphrase empty.

Add public key to file ~/.ssh/authorized_keys: echo $(cat self_todos.pub) >> authorized_keys

Hint:

Configure SSH Access to Github for the Project Manager: https://gist.github.com/oanhnn/80a89405ab9023894df7
