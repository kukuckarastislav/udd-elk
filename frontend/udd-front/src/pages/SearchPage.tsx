import { useState } from 'react';
import { SearchDTO } from '../models/SearchDTO';
import style from './SearchPage.module.scss';
import axios from 'axios';
import { DocumentDTO } from '../models/DocumentDTO';
import { Button } from '@mui/material';
import Result from '../components/Result';

export default function SearchPage() {

  const [searchDTO, setSearchDTO] = useState<SearchDTO>(new SearchDTO());
  const [searchResults, setSearchResults] = useState<DocumentDTO[]>([]);
  const booleanQueryOPERANDS = ['AND', 'OR', 'NOT']
  const booleanQueryFields = ['lawText:', 'contractText:', 'employeeName:', 'employerSurname:', 'governmentLevel: OPSTINSKA', 'governmentLevel: GRADSKA', 'governmentLevel: POKRAJINSKA', 'governmentLevel: DRZAVNA', 'governmentName:', 'address:', 'title:', 'typeOfDoc: LAW', 'typeOfDoc: CONTRACT'  ]

  const handleGetSearchResults = async () => {
    console.log(searchDTO);
    axios.post('http://localhost:8080/api/search', searchDTO)
      .then(response => {
        console.log(response);
        console.log(response.data.content);
        setSearchResults(response.data.content);
      })
      .catch(error => {
        console.log(error);
      });

  }
  
  return (
    <div className={style.SearchPageCss}>
      <div className="mx-40 my-10">
        <div className='flex flex-col justify-center gap-4 rounded-md shadow-sm p-6 bg-slate-50'>
          
          <div className='flex flex-col gap-4'>


          <div className='flex gap-6'>
              <div className='flex gap-2'>
                <input id="radio_standard_search" value="standard_search" name="search_type" type="radio"   className="cursor-pointer" checked={searchDTO.typeOfSearch === "standard_search"}
                    onChange={(event) => {
                      console.log("standard_search");
                      let newSearchDTO = JSON.parse(JSON.stringify(searchDTO))
                      newSearchDTO.typeOfSearch = event.target.value;
                      setSearchDTO(newSearchDTO);
                    }}
                  />
                <label htmlFor="radio_standard_search" className="block text-sm font-medium text-slate-700 cursor-pointer">Standard search</label>
              </div>
              <div className='flex gap-2'>
                <input id="radio_boolean_query" value="boolean_query" name="search_type" type="radio" className="cursor-pointer" checked={searchDTO.typeOfSearch === "boolean_query"}
                    onChange={(event) => {
                      console.log("boolean_query");
                      let newSearchDTO = JSON.parse(JSON.stringify(searchDTO))
                      newSearchDTO.typeOfSearch = event.target.value;
                      setSearchDTO(newSearchDTO);
                    }}
                  />
                <label htmlFor="radio_boolean_query" className="block text-sm font-medium text-slate-700 cursor-pointer">Advanced search</label>
              </div>
            </div>

            {searchDTO.typeOfSearch === 'standard_search' &&
            <div className='flex flex-col gap-2'>
              <div className='flex gap-2 cursor-pointer'>
                <input id="coolCheckbox_law" type="checkbox" checked={searchDTO.lawDoc} className="cursor-pointer"
                  onChange={(event) => {
                    if(!(event.target.checked || searchDTO.contractDoc)) return;
                    let newSearchDTO = JSON.parse(JSON.stringify(searchDTO))
                    newSearchDTO.lawDoc = event.target.checked;
                    setSearchDTO(newSearchDTO);
                  }}
                />
                <label htmlFor="coolCheckbox_law" className="block text-sm font-medium text-slate-700 cursor-pointer">Laws</label>
              </div>
              <div className='flex gap-2 cursor-pointer'>
                <input id="coolCheckbox_contract" type="checkbox" checked={searchDTO.contractDoc} className="cursor-pointer"
                  onChange={(event) => {
                    if(!(event.target.checked || searchDTO.lawDoc)) return;
                    let newSearchDTO = JSON.parse(JSON.stringify(searchDTO))
                    newSearchDTO.contractDoc = event.target.checked;
                    setSearchDTO(newSearchDTO);
                  }}
                />
                <label htmlFor="coolCheckbox_contract" className="block text-sm font-medium text-slate-700 cursor-pointer">Contracts</label>
              </div>
            </div>
            }

            {(searchDTO.contractDoc && searchDTO.typeOfSearch === 'standard_search') && 
            <div className='flex flex-col gap-4'>
            <div className='flex gap-4 mt-4'>
              <div className='block w-48'>
                <span className="block text-sm font-medium text-slate-700">Employee Name</span>
                <input type="text" value={searchDTO.employeeName} className="cool-input"
                  onChange={(event) => {
                    let newSearchDTO = JSON.parse(JSON.stringify(searchDTO))
                    newSearchDTO.employeeName = event.target.value;
                    setSearchDTO(newSearchDTO);
                  }}
                />
              </div>

              <div className='block w-48'>
                <span className="block text-sm font-medium text-slate-700">Employee Surname</span>
                <input type="text" value={searchDTO.employerSurname} className="cool-input"
                  onChange={(event) => {
                    let newSearchDTO = JSON.parse(JSON.stringify(searchDTO))
                    newSearchDTO.employerSurname = event.target.value;
                    setSearchDTO(newSearchDTO);
                  }}
                />
              </div>
            </div>
            
            <div className='flex gap-4'>
              <div className='block w-48'>
                <span className="block text-sm font-medium text-slate-700">Government name</span>
                <input type="text" value={searchDTO.governmentName} className="cool-input"
                  onChange={(event) => {
                    let newSearchDTO = JSON.parse(JSON.stringify(searchDTO))
                    newSearchDTO.governmentName = event.target.value;
                    setSearchDTO(newSearchDTO);
                  }}
                />
              </div>
              
              <div className='block w-48'>
                <span className="block text-sm font-medium text-slate-700">Government level</span>
                <select value={searchDTO.governmentLevel} className="cool-input"
                  onChange={(event) => {
                    let newSearchDTO = JSON.parse(JSON.stringify(searchDTO))
                    newSearchDTO.governmentLevel = event.target.value;
                    setSearchDTO(newSearchDTO);
                  }}>
                  <option value="">ALL</option>
                  <option value="OPSTINSKA">OPSTINSKA</option>
                  <option value="GRADSKA">GRADSKA</option>
                  <option value="POKRAJINSKA">POKRAJINSKA</option>
                  <option value="DRZAVNA">DRZAVNA</option>
                </select>
              </div>
            </div>
            </div>
            }

            {searchDTO.typeOfSearch === 'standard_search' &&
              <div className='block w-[25rem]'>
                <span className="block text-sm font-medium text-slate-700">Text</span>
                <textarea value={searchDTO.fullText} className="cool-input"
                  onChange={(event) => {
                    let newSearchDTO = JSON.parse(JSON.stringify(searchDTO))
                    newSearchDTO.fullText = event.target.value;
                    setSearchDTO(newSearchDTO);
                  }}
                />
              </div>
            }

            {searchDTO.typeOfSearch === 'boolean_query' &&
              <div className='block w-full'>
                <span className="block text-sm font-medium text-slate-700">Boolean Query</span>
                <textarea value={searchDTO.booleanQuery} rows={1} className="cool-input"
                  onChange={(event) => {
                    let newSearchDTO = JSON.parse(JSON.stringify(searchDTO))
                    newSearchDTO.booleanQuery = event.target.value;
                    setSearchDTO(newSearchDTO);
                  }}
                />
                <div className='mt-4 flex gap-2'>
                  {booleanQueryOPERANDS.map((operand, index) => {
                    return (
                      <Button variant='outlined' size='small' key={index} 
                        onClick={() => {
                          let newSearchDTO = JSON.parse(JSON.stringify(searchDTO))
                          newSearchDTO.booleanQuery += ` ${operand} `;
                          setSearchDTO(newSearchDTO);
                        }}
                      >{operand}</Button>
                    )
                  })}
                </div>
                <div className='mt-4 flex gap-2 flex-wrap'>
                  {booleanQueryFields.map((operand, index) => {
                    return (
                      <Button variant='outlined' color='secondary' sx={{textTransform: "none"}} size='small' key={index} 
                        onClick={() => {
                          let newSearchDTO = JSON.parse(JSON.stringify(searchDTO))
                          newSearchDTO.booleanQuery += ` ${operand} `;
                          setSearchDTO(newSearchDTO);
                        }}
                      >{operand}</Button>
                    )
                  })}
                </div>
              </div>
            }
              
            
          </div>

          <div className='mt-4'>
            <button className="cool-btn"
              onClick={handleGetSearchResults}
            >Search</button>
          </div>

        </div>
        

        <div className='mt-12 text-lg text-blue-800 font-bold'>{searchResults.length} Results</div>
        <div id="resultContainer" className='flex flex-col gap-1'>
          {searchResults.map((doc, index) => {
            return (
              <Result doc={doc} key={index} />
            )
          })}
        </div>

      </div>
    </div>
  )
}
