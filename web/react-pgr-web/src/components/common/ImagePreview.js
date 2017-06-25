import React, { Component } from 'react';

export default (props) => {
    let files = props.files;
    let renderImage = () =>
    {
      if(files && files.length>0){
        var parent = document.getElementById("imgParent");
        parent.innerHTML = "";
        for(let i=0; i<files.length; i++) {
          let div = document.createElement("div");
          div.className = "col-md-3 col-xs-12";
          let file = files[i];
          let filename=file.name;
          let reader = new FileReader();
          reader.onload = function(e) {
    				//setImage(e,input, filename);
            var bool = false;
            for(var i=0; i<parent.children.length; i++) {
              if(parent.children[i].getAttribute("data-name") == filename) {
                bool = true;
                break;
              }
            }
            if(!bool){
              let img = document.createElement("img");
              img.src = e.target.result;
              div.setAttribute("data-name", filename);
              img.style.width = "100%";
              img.style.height = "auto";
              div.appendChild(img);
              parent.appendChild(div);
            }
    			}
          reader.readAsDataURL(file);
        }
      }else{
        if(document.getElementById("imgParent"))
          document.getElementById("imgParent").innerHTML = "";
      }
    }

    return (
      <div id="imgParent">
        {renderImage()}
      </div>
    )
}
