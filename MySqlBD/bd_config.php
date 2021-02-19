<?php
 
 
 try {
$bdd = new PDO ('mysql:host=localhost;dbname=id10517781_inkless;charset=utf8','id10517781_inkless','inkless123');
$bdd->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
 
}

catch(PDOException $e)
    {
    echo "Connection failed: " . $e->getMessage();
    }

?>

