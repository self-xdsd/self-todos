In order to connect to localhost via SSH, you will need to generate a pair of public/private SSH keys for test. 
To generate a pair of keys and register them with your local SSH server, follow these steps:

https://docs.github.com/en/github/authenticating-to-github/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent

I suggest choosing a different name than ``id_rsa``. You can leave the passphrase empty.

Add private key to ssh-agent.
Add public key to file ~/.ssh/authorized_keys: echo $(cat self_todos_rsa4.pub) >> authorized_keys

Hint:

The library we use will throw an exception because it only supports RSA keys. To fix the problem, follow this article:
https://mkyong.com/java/jsch-invalid-privatekey-exception/


PM SSH Access to Github https://gist.github.com/oanhnn/80a89405ab9023894df7
