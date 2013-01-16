<?php
//var_dump("HERE");
    //var_dump($data);
  // $thumbnail_field = str_replace('.','',$this->config->item('skylight_thumbnail_field'));
   // $bitstream_field = str_replace('.','',$this->config->item('skylight_bitstream_field'));

    // JSON serialize a record
  //  $url=$this->config->item('skylight_objectproxy_url').$data[id].$this->uri->segments[3].$this->uri->segments[4];
   // $filearray = $data['solr'][$bitstream_field];
  //  $filearray = $data['solr'][$thumbnail_field];

    // Set the correct response headers
            //    header('Content-Type: ' . getBitstreamsMimeType($filearray, $seq));
            //    header('Content-MD5: ' . getBitstreamMD5($filearray, $seq));
            //    header('Content-Length: ' . getBitstreamLength($filearray, $seq));

                //var_dump($url);
                // Stream the file
            //    readfile($url);

                // Go no further
             //   die();

    $record = array();
//var_dump($solr);
    $thumbnail_field = str_replace('.','',$this->config->item('skylight_thumbnail_field'));
    $bitstream_field = str_replace('.','',$this->config->item('skylight_bitstream_field'));

//    $url=$this->config->item('skylight_objectproxy_url').$data[id].$this->uri->segments[3].$this->uri->segments[4];
    $filearray = $solr[$bitstream_field];
    //$bitstream=null;
    foreach($filearray as $index => $value) {
        if(strpos($value, "cfEquip")>0) {
            $bitstream = $value;
            break;
        }
    }
    $seq = getBitstreamSequence($bitstream);
    //$filearray = $solr[$thumbnail_field];
    //var_dump(getBitstreamsMimeType($filearray, $seq));
    $options = array(
            'Content-Type: ' =>  "text/xml",
            'Content-MD5: '  =>  getBitstreamMD5($filearray, $seq),
            'Content-Length: ' => getBitstreamLength($filearray, $seq)
            );

    $bitstreamLink = $this->config->item('base_url').str_replace('./','',getBitstreamUri($bitstream));
    $result=file_get_contents($bitstreamLink);
  // var_dump($result);
   //$seq = getBitstreamSequence($metadatavalue);
   // var_dump($bitstreamLink);


  //  foreach($recorddisplay as $key => $element) {
   //   if (isset($solr[$element])) {
   //         $key = array();
   //         foreach ($solr[$element] as $metadatavalue) {
   //             $key["" . $metadatavalue] = $element;
   //         }
   //     $record[$element] = $key;
  //      }
  //  }

    //TODO Put in digital objects
   // header('Content-type: application/xml');
   // $xml = new SimpleXMLElement('<record/>');
   // array_walk_recursive($record, array ($xml, 'addChild'));
   // print $xml->asXML();

    print htmlentities($result);
    die();
?>
