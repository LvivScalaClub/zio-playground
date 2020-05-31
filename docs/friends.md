# Friends design

## Use cases
User should be able to:
 - fetch all friends for the user
 - Add a new friend
 - Delete a friend
 
 
## Entities

### Friends
   - id: long
   - dateCreated: date
   - dateUpdated: date 
   - userId: long
   - friendId: long
   - userName: string
   
## Endpoints

| METHOD(s) | Path | Request Body | Notes |
|--------|------|-------------|-------|
|GET| /v1/friends/{userId} | - | get friends for the user. Paginated |
|POST| /v1/friends/{userId}/{friendId} |  | add a new friend|
|DELETE| /v1/friends/{userId}/{friendId} | - | delete a friend|

