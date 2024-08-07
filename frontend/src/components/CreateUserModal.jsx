import React from 'react'
import {Button, Flex, FormControl, FormLabel, Input, Modal, ModalBody, ModalCloseButton, ModalContent, ModalFooter, ModalHeader, ModalOverlay, Textarea, useDisclosure} from '@chakra-ui/react'
import {BiAddToQueue} from "react-icons/bi"

const CreateUserModal = () => {
  const { isOpen, onOpen, onClose } = useDisclosure()
  return ( <>
    <Button onClick={onOpen}>
      <BiAddToQueue size={20} />
    </Button>
    
    <Modal isOpen={isOpen}
    onClose={onClose}>
      <ModalOverlay />
      <ModalContent>
        <ModalHeader> New Task </ModalHeader>
        <ModalCloseButton />

        <ModalBody pb={6}>
          <Flex alignItems={"center"} gap={4}>
            <FormControl>
              <FormLabel>Task Name</FormLabel>
              <Input placeholder='Get Milk' />
            </FormControl>
          </Flex>

          <FormControl mt={4}>
              <FormLabel>Task Description</FormLabel>
              <Textarea 
              resize={"none"}
              overflow={"hidden"}
              placeholder='Needed for ...'
               />
          </FormControl>
        </ModalBody>
        <ModalFooter>
          <Button colorScheme='blue' mr={3}>
            Add
          </Button>
          <Button onClick={onClose}>
            Cancel
          </Button>
        </ModalFooter>

      </ModalContent>

    </Modal>
    </>
    
  )
}

export default CreateUserModal