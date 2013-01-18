<?php
    // Example labels for authority control with dspace
    // dccontributorauthor
    // dctitle
    // dcdateavailable
    // dcdateissued

    // function definition to convert records array to xml
    function array_to_xml($record_array, &$xml) {
        foreach($record_array as $key => $value) {
            if(is_array($value)) {
                if(!is_numeric($key)){
                    $subnode = $xml->addChild("$key");
                    array_to_xml($value, $subnode);
                }
                else{
                    array_to_xml($value, $xml);
                }
            }
            else {
                $xml->addChild("$value","$key");
            }
        }
    }

    $records = array();
    $records[] = array( sizeof($docs) => 'numhits');

    if(isset($_GET['field'])) 
    { 
        $label = $_GET['field']; 
        $xml = new SimpleXMLElement('<?xml version="1.0"?> <records/>');
        foreach ($docs as $doc) {
            $record = array();
            //var_dump($doc);
            foreach($doc as $key => $element) {
                if ($key == $label) {
                    foreach ($element as $index => $value) {     
                        settype($value, "string");                   
                        $record[$value] = $key;          
                    }    
                }
                else if( $key == "id") {
                    $url = $this->config->item('base_url') . "record/" . $element;
      
                    $record[$url] = "URL";                             
                }
            }
            $records[] = array('record' => $record);
        }
    } 
    else 
    {
       $xml = new SimpleXMLElement('<?xml version="1.0"?> <records/>');
        foreach ($docs as $doc) {
            $record = array();
            //var_dump($doc);
            foreach($doc as $key => $element) {
                if ($key == "title_ac" || $key == "dctype" || $key == "dcdescription" || $key == "author_ac" || $key == "dcidentifieruri") {
                    foreach ($element as $index => $value) {     
                      settype($value, "string");                   
                        $record[$value] = $key;   
                    }          
                }
                else if( $key == "id") {
                    $url = $this->config->item('base_url') . "record/" . $element;
      
                    $record[$url] = "URL";                             
                }
            }
            $records[] = array('record' => $record);
        } 
    }
    
    //var_dump($docs);

    //header('Content-type: application/xml');
 

    //array_walk_recursive($records, array($xml, 'addChild'));
    array_to_xml($records, $xml);
	print $xml->asXML(); //htmlentities($xml->asXML());
?>
