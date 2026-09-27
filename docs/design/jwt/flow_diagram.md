```mermaid
flowchart TD
    JwtService[JWT Service]
    AccessToken["Access Token"]
    RefreshToken["Refresh Token"]

    JwtService -->|Validates| AccessToken
    JwtService -->|Validates| RefreshToken
    
    JwtService -->|Generates| AccessToken
    JwtService -->|Generates| RefreshToken
    
```