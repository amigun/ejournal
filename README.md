# EJournal - electronic journal for college

EJournal - is a service for track a homework in your college which will be support by other students from your group.

It repository is a server for EJournal, not a client. You can make a client for this server by use a HTTPS requests.
## Dev.
To create a temporary database:
```bash
systemctl start docker
```
```bash
docker run --rm --name inpg -p 5432:5432 -e POSTGRES_USER=inuser -e POSTGRES_PASSWORD=inpasswd -e POSTGRES_DB=indb -d postgres:13.6
```
To stop a container:
```bash
docker stop $(docker ps -aq)
```
To check a work of container:
```bash
psql -h 127.0.0.1 -U inuser -d indb