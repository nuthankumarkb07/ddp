#!/usr/bin/env bash

rm *.pem
#root CA generation
cat << EOF > server-ext.cnf
subjectAltName=DNS:localhost,IP:127.0.0.1,IP:10.177.200.39,IP:0.0.0.0
EOF
cat << EOF > client-ext.cnf
subjectAltName=DNS:localhost,IP:127.0.0.1,IP:10.177.200.39
EOF

openssl req -x509 -newkey rsa:4096 -days 10 -nodes -keyout ca-key.pem -out ca-cert.pem -subj "/C=IN/ST=ST/L=BA/O=GE/OU=HC/CN=root" 
openssl x509 -in ca-cert.pem -noout -text 

openssl req -newkey rsa:4096 -days 1 -nodes -keyout server-key.pem -out server-req.pem -subj "/C=IN/ST=ST/L=BA/O=GE/OU=HC/CN=localhost" 
openssl x509 -req -in server-req.pem -days 1 -CA ca-cert.pem -CAkey ca-key.pem -CAcreateserial -out server-cert.pem -extfile server-ext.cnf

openssl x509 -in server-cert.pem -noout -text 

openssl req -newkey rsa:4096 -days 1 -nodes -keyout client-key.pem -out client-req.pem -subj "/C=IN/ST=ST/L=BA/O=GE/OU=HC/CN=localhost" 
openssl x509 -req -in client-req.pem -days 1 -CA ca-cert.pem -CAkey ca-key.pem -CAcreateserial -out client-cert.pem -extfile client-ext.cnf


openssl x509 -in client-cert.pem -noout -text 

openssl verify -CAfile ca-cert.pem server-cert.pem
openssl verify -CAfile ca-cert.pem client-cert.pem

keytool -keyalg RSA -keystore redpanda.client.keystore.jks -alias localhost -validity 365 -genkey
keytool -keystore redpanda.client.truststore.jks -alias CARoot -import -file ca-cert.pem

keytool -keystore redpanda.client.keystore.jks -alias localhost -certreq -file client-unsigned
openssl x509 -req -CA ca-cert.pem -CAkey ca-key.pem -in client-unsigned -out client-signed 

keytool -keystore redpanda.client.keystore.jks -alias CARoot -import -file ca-cert.pem
keytool -keystore redpanda.client.keystore.jks -alias localhost -import -file client-signed

keytool -exportcert -keystore redpanda.client.keystore.jks -alias localhost -file server-truststore.pem

chmod 777 *
