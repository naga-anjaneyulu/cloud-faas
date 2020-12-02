import logo from './logo.svg';
import './App.css';
import React, {Component} from 'react';

import UIService from './UIService.js';

class App extends Component {

  constructor(props){
    super(props);
    this.state = {
        url : '',
        src : '',
        data : {}
    }
}


handleChange = (event) =>{
  this.setState({
      [event.target.name] : event.target.value
  })
  console.log(this.state.url)
  console.log(event.target.name)
  console.log(event.target.value)
 }

submit = (event) =>{
    
      var obj = {url : this.state.url}
      var res = {}
      console.log(obj)
       UIService.submitUrl(obj).then(  (response) => {
        this.setState({
          data : response.data })
      
       })
     
      
      }
   
      getImage = (event) => {
       
        
        var obj3 = {}
        var obj4 = this.state.data
        Object.keys(obj4).map(function(keyName, keyIndex) {
          obj3[keyName] = obj4[keyName]
        })
        var obj2 = {'url' : this.state.url , 'data' : obj3 }
        UIService.getFreImage(obj2).then(  (response2) => {
         console.log(response2.data)
         console.log(response2.data.imageSrc)
         this.setState({
           src : response2.data.imageSrc
         })})
           
      }

  render() {
    const obj = this.state.data
    return (
      <div className="App">
       URL : <input type ="text" name ="url" value ={this.state.url} onChange ={this.handleChange}/>
       <button className ="my-button" onClick={this.submit}>Submit</button> <br></br>
       <div>
       {
        
       Object.keys(obj).map(function(keyName, keyIndex) {
      
          return(<p>{keyName} : {obj[keyName]} </p>)
        })}
       </div>
       
       <button className ="my-button" onClick={this.getImage}>Get Image</button> <br></br>
       <img src={this.state.src} alt="Image on Cloud9" width="500" height="600"></img>
      </div>
    );
  }
}






export default App;