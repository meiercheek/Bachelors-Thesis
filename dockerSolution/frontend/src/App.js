import { useState, useEffect } from 'react'
import { base64 } from "rfc4648";
import mushrooms from './Data'
import * as tf from '@tensorflow/tfjs'
import Drawer from '@mui/material/Drawer'
import ImageList from '@mui/material/ImageList'
import ImageListItem from '@mui/material/ImageListItem'
import ImageListItemBar from '@mui/material/ImageListItemBar'
import itemData from './ItemData'
import Box from '@mui/material/Box'
import Button from '@mui/material/Button'
import Typography from '@mui/material/Typography'
import Modal from '@mui/material/Modal'

const style = {
    color: 'black',
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 450,
    bgcolor: 'background.paper',
    border: '4px solid #000',
    borderRadius: '5px',
    boxShadow: 99,
    p: 4,
}
  
export const server = 'http://'+ document.location.hostname + ":8501"

let getBase64 = (file) => {
    return new Promise((resolve, reject) => {
      const reader = new FileReader()
      reader.readAsDataURL(file)
      reader.onload = () => resolve(reader.result)
      reader.onerror = error => reject(error)
    })
}

const postImage = (img) => {
    
    return fetch(`${server}/v1/models/model/versions/1:predict`, {  
        method: 'POST',
        mode: 'cors',
        credentials: 'same-origin',
        body: JSON.stringify({
            instances: img,
        })
    })
    .then((response) => response.json())
    .then((responseData) => {return (responseData)})
    .catch(error => {return (error)})
}

const getStatus = () => {
    return fetch(`${server}/v1/models/model/versions/1`, {  
        method: 'GET',
        mode: 'cors',
        credentials: 'same-origin',
    })
    .then((response) => response.json())
    .then((responseData) => {return (responseData)})
    .catch(error => {return (error)})
}

let getMatch = (str) =>{
    return <>
        <b>match: </b>{(str.match * 100).toFixed(2)}%
        </>
}  

function App() {
    const [image, setImage] = useState(null)
    const [results, setResults] = useState([])
    const [history, setHistory] = useState([])
    const [error, setError] = useState(false)
    const [loading, setLoading] = useState(false)
    const [ready, setReady] = useState(false)
    const [drawerOpen, setDrawerOpen] = useState(false)
    const [openModal, setModalOpen] = useState(false);
    const handleModalOpen = () => setModalOpen(true);
    const handleModalClose = () => setModalOpen(false);


    const [message, setMessage] = useState('')
    let counter

    const identify = (img, rawimg) => {
        
        let i = new Image()
        
        i.src = img

        i.onload = ()=>{

            let result = tf.tidy(() => {
                let ll = tf.browser.fromPixels(i)
                const b = tf.scalar(255);
                return ll.resizeBilinear([224,244]).div(b).arraySync()
            });

            
            postImage([result]).then( response => {
                if(response){
                    if(response.message === "Failed to fetch"){
                        setLoading(false)
                        alert('no connection to the server')
                        return
                    }

                    else if(response.hasOwnProperty('predictions')){
                        let converted = response.predictions[0].map( x => Number.parseFloat(x).toFixed(5))
                        //console.log(mushrooms)
                        let shroomMap = converted.map(function (x, i) { 
                            return {match: x, shroom: mushrooms[i]}
                        })
                        shroomMap.sort((x,y)=>{
                            if(x.match < y.match){
                                return -1
                            }
                            if(x.match > y.match){
                                return 1
                            }
        
                            return 0
                        }).reverse()
        
                        setResults(shroomMap.slice(0,3))
                        setLoading(false)
                    }
                    else{
                        alert('error occurred during analysis')   
                        setLoading(false)
                    }

                    
    
                }
                else{
                    setError(true)
                }
                tf.disposeVariables()
                
            }) 
        } 
    }

    useEffect(() => {

        setMessage('checking the connection to the server')
                getStatus().then(r=>{
                    if(r.model_version_status[0].state === "AVAILABLE" &&r.model_version_status[0].status.error_code === "OK" ){
                        setReady(true)
                    }
                }).catch(()=>{
                    setMessage('no connection to the server')
                    setError(true)
                }
                    
                )
    }, [])


    
 
    useEffect(() => {
        if (image && history.find(x => x.image === image) === undefined) {
            setHistory([{image: image, result: results}, ...history])
        }
    }, [results])


    if(!ready && !error){
        return(<div className='infoLoading'>
        
        <div id="centerLoading" className="spinner-border text-light" role="status">
                        <span className="sr-only">loading...</span>
        </div>
        <p>{message}</p>    
        </div>
        ) 
    }
    else if(error){
        return(
        <div className='infoLoading'>
            <p>{message}</p>    
        </div>
        ) 
    }

    else{
        return (
        
            <div className="App">
                <Modal
                    open={openModal}
                    onClose={handleModalClose}
                    aria-labelledby="modal-modal-title"
                    aria-describedby="modal-modal-description"
                >
                    <Box sx={style}>
                    <Typography id="modal-modal-title" variant="h6" component="h2">
                        <b>mushroom classifier</b>
                    </Typography>
                    <Typography id="modal-modal-description" sx={{ mt: 2 }}>
                        made by lubomir majercik as their bachelor's thesis project in 2022 at fiit stu                       
                    </Typography>
                    <Typography id="modal-modal-description" sx={{ mt: 2 }}>
                        special thanks to: Ing. Giang Nguyen Thu, PhD.
                    </Typography>
                    <Typography id="modal-modal-description" sx={{ mt: 2 }}>
                        libraries used: <a target="_blank" href="https://mui.com/">material ui</a>,
                          <a target="_blank" href="https://getbootstrap.com/"> bootstrap</a>, 
                           <a target="_blank" href="https://github.com/tensorflow/tfjs"> tensorflow.js</a>, 
                             <a target="_blank" href="https://github.com/swansontec/rfc4648.js"> rfc4648 </a> 
                            and uses the  
                             <a target="_blank" href="https://create-react-app.dev/"> create react app </a>  template
                    </Typography>
                    <Typography id="modal-modal-description" sx={{ mt: 2 }}>
                        backend runs on <a target="_blank" href="https://github.com/tensorflow/serving">tensorflow serving</a> docker image, serving fine-tuned and trained dense121 model on freely available mushroom images
                    </Typography>
                    </Box>
                </Modal>
                <Drawer anchor='left' open={drawerOpen} onClose={() => setDrawerOpen(false)}>
                    <ImageList sx={{ width: 710 }}>
                        {itemData.map((item) => (
                            <button key={item.img} className="btn btn-light btn-lg"  style= {{
                            margin:'10px',
                            }}
                            onClick={() =>{
                                setDrawerOpen(false)
                                fetch(item.img).then((fetched) =>{
                                    fetched.blob().then((blobbed) =>{
                                        getBase64(blobbed).then(based =>{
                                            setError(false)
                                            setLoading(true)
                                            let stripped = based.split(',')[1]  
                                            setResults([])
                                            setImage(based)
                                            identify(based, base64.parse(stripped))                      
                                        })
                                    })   
                                })
                            }}
                            color="secondary" variant="text">
                            <ImageListItem >
                            <img
                                src={`${item.img}`}
                                srcSet={`${item.img}`}
                                alt={item.title}
                                loading="lazy"
                                style= {{
                                height:'150px',
                                maxWidth: '150px'
                                }}
                            />
                            <ImageListItemBar
                                title={item.name.replace("_", " ")}
                                subtitle={<p> Image by: {item.author}  </p>}
                                
                                position="below"
                            />

                            <p style={{
                                fontSize: '15px',
                            }}><a target="_blank" href={item.link}> Link</a></p>
                            <p style={{
                                fontSize: '15px',
                            }}><a target="_blank" href={item.license}> License</a></p>
                            
                             
                            
                            </ImageListItem>
                            </button>
                            
                    ))}
                    </ImageList>
                </Drawer>
               <div className='header'>
                   
                <div className="card">
                    <div className="card-body">
                        <h1 className="card-title">mushroom classifier</h1>                        
                        <button className="btn btn-outline-dark" onClick={handleModalOpen}>about</button>
                        <div className='inputWrapper'>
                            <label style= {{
                            margin:'10px',
                            }} className="btn btn-outline-dark btn-lg">
                                <input
                                    type="file"
                                    name="myImage"
                                    accept='image/*'
                                    capture='camera'
                                    className='uploadInput'
                                    onChange={
                                        (event) => { 
                                            getBase64(event.target.files[0]).then(r =>{
                                                setError(false)
                                                setLoading(true)
                                                let stripped = r.split(',')[1] 
                                                setResults([])
                                                setImage(r)
                                                identify(r, base64.parse(stripped))
                                                
                                    })}} 
                                /> upload an image
                            </label>
                            
                                
                            <button style= {{
                            margin:'10px',
                            }} onClick={() => setDrawerOpen(true)} className="btn btn-outline-dark btn-lg">
                                try some predefined images
                            </button>
                            
          
                        </div>
                        
                    </div>
                    </div>
                </div>                            
    
                <div className="mainWrapper">
                    
                    <div className="mainContent">
                     
                        
                        {loading &&  <div id="centerLoadingShroom" className="spinner-border text-light" role="status">
                            <span className="sr-only">loading...</span>
                            </div>}
                        {error && <>error occured during analysis, try again</>}
                        {results.length > 0 && <div >
                        <div className='card'>
                            <div className="card-body">
                            
                            {image && <img src={image} alt="hribik" crossOrigin="anonymous"  />}
                        
                                <h2 className="card-title">
                                    <b>{results[0].shroom.id.replace(/_/g, " ").toLowerCase()}</b>
                                </h2>
                                <p className="card-text">
                                    <p>{results[0].shroom.edibility}</p>
                                    
                                    {getMatch(results[0])}
    
                                <h5 className='padding-tops'>
                                    {results[1].match > 0.1 && results[2].match < 0.1 && <b>other possible result:</b>}
                                    {results[2].match > 0.1 && results[1].match < 0.1 && <b>other possible result:</b>}
                                    {results[2].match > 0.1 && results[1].match > 0.1 && <b>other possible results:</b>}
                                </h5>
                                {results.map((result, index) => {
                                    if(index === 0){
                                        return null
                                    }
                                    if(result.match < 0.1){
                                        return null
                                    }
                                    else{
                                        
                                    }
                                    return (
                                        <div className='parentflex'>
                                            
                                            <h5 className="card-title" >
                                                {result.shroom.id.replace(/_/g, " ").toLowerCase()}
                                            </h5>
                                            <p className="card-text">
                                                <p>{result.shroom.edibility}</p>
                                                {getMatch(result)}
                                            </p>
                                        </div>                              
                                    )
                                })}    
                                </p>
                                                
                            
                            </div>   
                        </div>
                        
                        </div>}
                    </div>
                    
                </div>
                {history.length > 0 && <div className="cardHistory">
                    <h2>previous uploads</h2>
                    <div className="recentImages">
                        {history.map((history, index) => {
                            return (
                                <div className="recentPrediction" key={`${history.image}${index}`}>
                                    {history.result.length > 0 && <div >  
                                        <div className="card" >
                                            <div className="card-body">
                                            <img src={history.image} alt='previous' onClick={() =>{ 
                                        setResults([])
                                        setImage(history.image)}} />
                                            <h4 className="card-title">
                                                <b>{history.result[0].shroom.id.replace(/_/g, " ").toLowerCase()}</b>
                                            </h4>
                                            <p className="card-text">
                                                <p>{history.result[0].shroom.edibility}</p>
                                                {getMatch(history.result[0])}
                                            </p>
                                            
                                            </div>
                                        </div>              
                                </div>
                                    }
                                </div>
                            )
                        })}
                    </div>
                </div>}
            </div>
        );
    }

    
}

export default App
