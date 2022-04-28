https://redpanda.com/blog/tls-config/




local installations

curl -1sLf 'https://packages.vectorized.io/nzc4ZYQK3WRGd9sy/redpanda/cfg/setup/bash.rpm.sh' | sudo -E bash && sudo yum install redpanda -y && sudo systemctl start redpanda


openssl s_server -accept 3000 -CAfile /home/ubuntu/codebase/security/ca-cert.pem  -cert /home/ubuntu/codebase/security/server-cert.pem -key /home/ubuntu/codebase/security/server-key.pem  -state


openssl s_client -connect localhost:3000 -key /home/ubuntu/codebase/security/client-key.pem  -cert /home/ubuntu/codebase/security/client-cert.pem -CAfile /home/ubuntu/codebase/security/ca-cert.pem -state



 openssl s_client -connect localhost:9092 -key /home/ubuntu/codebase/security/client-key.pem  -cert /home/ubuntu/codebase/security/client-cert.pem -CAfile /home/ubuntu/codebase/security/ca-cert.pem -state


 rpk topic list --tls-key /home/ubuntu/codebase/security/client-key.pem --tls-cert /home/ubuntu/codebase/security/client-cert.pem --tls-truststore /home/ubuntu/codebase/security/ca-cert.pem

 rpk topic list --tls-truststore /home/ubuntu/codebase/security/ca-cert.pem

 rpk topic consume nuthan --tls-key /home/ubuntu/codebase/security/client-key.pem --tls-cert /home/ubuntu/codebase/security/client-cert.pem --tls-truststore /home/ubuntu/codebase/security/ca-cert.pem


rpk topic produce nuthan --tls-key /home/ubuntu/codebase/security/client-key.pem --tls-cert /home/ubuntu/codebase/security/client-cert.pem --tls-truststore /home/ubuntu/codebase/security/ca-cert.pem

rpk topic consume nuthan  --tls-truststore /home/ubuntu/codebase/security/ca-cert.pem

Links:

https://www.golinuxcloud.com/mutual-tls-authentication-mtls/
https://www.vertica.com/docs/9.2.x/HTML/Content/Authoring/KafkaIntegrationGuide/TLS-SSL/KafkaTLS-SSLExamplePart3ConfigureKafka.htm
