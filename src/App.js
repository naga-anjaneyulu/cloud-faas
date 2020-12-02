import logo from './logo.svg';
import './App.css';
import React, {Component} from 'react';

import UIService from './UIService.js';

class App extends Component {

  constructor(props){
    super(props);
    this.state = {
        url : '',
        state : false
    }
}


handleChange = (event) =>{
  this.setState({
      [event.target.name] : event.target.value
  })
 }

submit = (event) =>{
    
      var obj = {url : this.state.url}
       UIService.submitUrl(obj).then(  (response) => {
         console.log(response);
            })
}
   

  render() {
    return (
      <div className="App">
       URL : <input type ="text" name ="url" value ={this.state.url} onChange ={this.handleChange}/>
       <button className ="my-button" onClick={this.submit}>Submit</button> <br></br>
      </div>
    );
  }
}






export default App;