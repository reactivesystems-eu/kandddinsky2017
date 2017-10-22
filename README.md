# KanDDDinsky CQRS workshop

This is a repository with code created during the [CQRS Workshop](http://kandddinsky.de/14.html) on [KanDDDinsky 2017](http://www.kandddinsky.de) and is only useful to the participants of the workshop, to continue to explore CQRS with [Lagom](http://www.lagomframework.com)

Example for how to create a reservation with [httpie](https://httpie.org/):

```
http localhost:9000/api/accomodation/meine-wohnung/reservation \
accomodation=meine-wohnung \
guest=Franz \
host=Bernd \
startingDate=2017-11-11 \
duration:=3
```
