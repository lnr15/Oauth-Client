<html>
 <body>
    <form action = "http://localhost:5656/oauth/authorize" method="get">
      <input type="hidden" name="client_id" value="clientapp">
      <input type="hidden" name="scope" value="read">
      <input type="hidden" name="response_type" value="code">
      <input type="submit" value="SignIn with SpringOAuth">
    </form>
  </body>
 </html>
    