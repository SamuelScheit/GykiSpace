# Gyki Space

## Abstract
This protocol defines the protocol to communicate between the GykiSpace server and client over a text interface. This is Version 1 and there are no revisions planned. 

## Introduction
The server listens on port 8051 for connections and can handle multiple connections simultaneously. Each connections is handled in their own Thread and is waiting for events. Events are encoded as XML strings and sent as a single packet. When the client connects to the server over a TCP socket the server responds with an hello event, which returns metadata about the server. After that the client should send pings based on the ping_interval. If a server doesn't respond within 5 seconds on a ping the connection should be closed and a new one should be established. Subsequently the client sends the identify event and waits for the Ready event.

## Structures
Every event has a type key which uniquely identifies the event type.

### Hello Event
|     **Key**    | **Type** |                       **Description**                       |
|:--------------:|:--------:|:-----------------------------------------------------------:|
|      type      |     0    |                                                             |
|  online\_users |    int   |               Number of currently online users              |
| ping\_interval |    int   | How often the client should ping the server in milliseconds |

Possible future additions: icon, name

### Ping Event
**Key**|**Type**|**Description**
:-----:|:-----:|:-----:
type|1| 

### Identify Event
**Key**|**Type**|**Description**
:-----:|:-----:|:-----:
type|2| 
username|string|Minimum 2 char name of the connecting user

Possible future additions: avatar

### Ready Event

**Key**|**Type**|**Description**
:-----:|:-----:|:-----:
type|3| 
rooms| |Array of rooms that are joinable

### Message Event
**Key**|**Type**|**Description**
:-----:|:-----:|:-----:
type|4| 
content|String|Content of the message
sender|String|Username of the sender

Possible future additions: file

### Create Room Event
Sent by the client to create a new room

**Key**|**Type**|**Description**
:-----:|:-----:|:-----:
type|5| 
name|String|Name of this message room
recipients|String[]|Array of usernames that should be added to the room
public|boolean|Whether or not the room should be publicly joinable by everyone

### Add Room Event
Received by the client if user was added to a room.

**Key**|**Type**|**Description**
:-----:|:-----:|:-----:
type|6| 
name|String|Name of the room
recipients|String[]|Array of usernames that are in this room

### User Join Event
**Key**|**Type**|**Description**
:-----:|:-----:|:-----:
type|7| 
name|String|Name of the room
username|String|Name of the user that joined
