import React, { useState } from 'react'
import {Button, Flex, FormControl, FormLabel, Input, Modal, ModalBody, ModalCloseButton, ModalContent, ModalFooter, ModalHeader, ModalOverlay, Textarea, useDisclosure, useToast} from '@chakra-ui/react'
import {BiAddToQueue} from "react-icons/bi"
import { BASE_URL } from '../App'

const CreateUserModal = ({setTasks}) => {
  const { isOpen, onOpen, onClose } = useDisclosure()
  const[isLoading, setIsLoading] = useState(false)

  const[inputs, setInputs] = useState({
    name: "",
    description: ""
  })
  const toast = useToast()

  const handleCreateTask = async (e) => {
    e.preventDefault() // prevents the page reloading 
    setIsLoading(true)
    try {
      const res = await fetch(BASE_URL + "/tasks", {
        method: "POST", 
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(inputs),
      })

      const data = await res.json()
      if(!res.ok) {
        throw new Error(data.error)
      }
      toast({
        title: 'Task added',
        description: "We've added your task for you.",
        status: 'success',
        duration: 9000,
        isClosable: true,
      })
      onClose()
      setTasks((prevTasks) => [...prevTasks, data])

    } catch (error) {
      toast({
        title: "an error occurred",
        description: error.message, 
        status: "error",
        duration: 4000,
      })
    } finally {
      setIsLoading(false)
      setInputs({
        name: "",
        description: ""
      })
    }
  }



  return ( <>
    <Button onClick={onOpen}>
      <BiAddToQueue size={20} />
    </Button>
    
    <Modal isOpen={isOpen}
    onClose={onClose}>
      <ModalOverlay />
      <form onSubmit={handleCreateTask}>
        <ModalContent>
          <ModalHeader> New Task </ModalHeader>
          <ModalCloseButton />

          <ModalBody pb={6}>
            <Flex alignItems={"center"} gap={4}>
              <FormControl>
                <FormLabel>Task Name</FormLabel>
                <Input placeholder='Get Milk' 
                  value={inputs.name}
                  onChange={(e) => setInputs({...inputs, name: e.target.value})}
                />
              </FormControl>
            </Flex>

            <FormControl mt={4}>
                <FormLabel>Task Description</FormLabel>
                <Textarea 
                resize={"none"}
                overflow={"hidden"}
                placeholder='Needed for ...'
                value={inputs.description}
                onChange={(e) => setInputs({...inputs, description: e.target.value})}
                />
            </FormControl>
          </ModalBody>
          <ModalFooter>
            <Button colorScheme='blue' mr={3} type='submit'
              isLoading={isLoading}>
              Add
            </Button>
            <Button onClick={onClose}>
              Cancel
            </Button>
          </ModalFooter>

        </ModalContent>
      </form>

    </Modal>
    </>
    
  )
}

export default CreateUserModal