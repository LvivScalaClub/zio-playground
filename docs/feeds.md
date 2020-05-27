# Feeds design

## Use cases
User should be able to:
 - fetch own or friends posts
 - fetch all feeds (own and friends posts)
 - create/update/delete own post
 - like/unlike a post 
 - leave/update/delete a comment for post
 
## Entities

### Post
   - id: long
   - dateCreated: date
   - dateUpdated: date - to see that was changed
   - userId: long
   - title: string
   - body: string
   - commentsCount: long
   - likesCount: long

### Like
   - postId: long
   - userId: string
    
### Comment
   - id: long
   - dateCreated: date
   - dateUpdated: date - to see that was changed 
   - postId: long
   - userId: long
   - body: string

## Endpoints

| METHOD(s) | Path | Request Body | Notes |
|--------|------|-------------|-------|
|GET| /v1/user/{userId}/feed | - | get own and friends posts. Sorted by dateCreated desc. Paginated |
|GET| /v1/user/{userId}/post | - | get all posts per user. Sorted by dateCreated desc. Paginated |
|POST| /v1/user/{userId}/post | PostCreateParams | create new posts|
|PUT| /v1/user/{userId}/post/{postId} | PostUpdateParams | update a post|
|DELETE| /v1/user/{userId}/post/{postId} | - | delete a post|
|GET| /v1/user/{userId}/post/{postId}/like | - | get likes for post|
|POST/DELETE| /v1/user/{userId}/post/{postId}/like | - | like/unlike post|
|GET| /v1/user/{userId}/post/{postId}/comment | - | get comments for post. Sorted by dateCreated desc. Paginated |
|POST| /v1/user/{userId}/post/{postId}/comment | CommentCreateParams | leave a comment|
|PUT| /v1/user/{userId}/post/{postId}/comment/{commentId} | CommentUpdateParams | update a comment|
|DELETE| /v1/user/{userId}/post/{postId}/comment/{commentId} | - | delete a comment|

### Request params
PostCreateParams:
 - title: string
 - body: string

PostUpdateParams:
 - title: string
 - body: string

CommentCreateParams:
 - body: string
 
CommentUpdateParams:
 - body: string
