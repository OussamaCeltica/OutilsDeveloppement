<?php

include("bd_config.php");

$req = $bdd ->prepare($_POST['query']);

$nbrElem=$_POST['nbrElem'];

$i=1;
while($i <= $nbrElem){
$req->bindValue($i, $_POST[''.$i]);

$i++;
}

try{
$req->execute();
echo "succ";
}
catch (exception $e){
    echo "err";
}




?>