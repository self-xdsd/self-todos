In order to connect to localhost via SSH, you will need to generate a pair of public/private SSH keys for test. 
To generate a pair of keys and register them with your local SSH server, follow these steps:

https://docs.github.com/en/github/authenticating-to-github/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent

I suggest choosing a different name than ``id_rsa``. You can leave the passphrase empty.

Hint:

The library we use will throw an exception because it only supports RSA keys. To fix the problem, follow this article:
https://mkyong.com/java/jsch-invalid-privatekey-exception/
