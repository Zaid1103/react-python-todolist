import {
	Button,
	Flex,
	FormControl,
	FormLabel,
	IconButton,
	Input,
	Modal,
	ModalBody,
	ModalCloseButton,
	ModalContent,
	ModalFooter,
	ModalHeader,
	ModalOverlay,
	Radio,
	RadioGroup,
	Textarea,
	useDisclosure,
	useToast,
} from "@chakra-ui/react";
import { useState } from "react";
import { BiEditAlt } from "react-icons/bi";
import { BASE_URL } from "../App";

function EditModal({ setTasks, task }) {
	const { isOpen, onOpen, onClose } = useDisclosure();
	const [isLoading, setIsLoading] = useState(false)
	const [inputs, setInputs] = useState({
		name: task.name,
		description: task.description
	})
	const toast = useToast() 


	const handleEditTask = async (e) => {
		e.preventDefault()
		setIsLoading(true)
		try {
			const res = await fetch(BASE_URL + "/tasks/" + task.id, {
				method: "PATCH", 
				headers: {
					"Content-Type": "application/json",
				},
				body: JSON.stringify(inputs)
			})
			const data = await res.json()
			if(!res.ok) {
				throw new Error(data.error)
			}
			setTasks((prevTasks) => prevTasks.map((t) => t.id == task.id ? data : t))
			toast({
        title: 'Task updated',
        description: "We've updated your task for you.",
        status: 'success',
        duration: 9000,
        isClosable: true,
      });
		} catch (error) {
			toast({
        title: "an error occurred",
        description: error.message, 
        status: "error",
        duration: 4000,
      })
		} finally {
			setIsLoading(false)
		}
	}

	return (
		<>
			<IconButton
				onClick={onOpen}
				variant='ghost'
				colorScheme='blue'
				aria-label='See menu'
				size={"sm"}
				icon={<BiEditAlt size={20} />}
			/>

			<Modal isOpen={isOpen} onClose={onClose}>
				<ModalOverlay />
				<form onSubmit={handleEditTask}>
					<ModalContent>
						<ModalHeader>New Task</ModalHeader>
						<ModalCloseButton />
						<ModalBody pb={6}>
							<Flex alignItems={"center"} gap={4}>
								<FormControl>
									<FormLabel>Task Name</FormLabel>
									<Input placeholder='John Doe' 
										value={inputs.name}
										onChange={(e) => setInputs((prev) => ({... prev, name: e.target.value}))}
									/>
								</FormControl>
							</Flex>
							<FormControl mt={4}>
								<FormLabel>Description</FormLabel>
								<Textarea
									resize={"none"}
									overflowY={"hidden"}
									placeholder="Needed for ..."
									value={inputs.description}
									onChange={(e) => setInputs((prev) => ({... prev, description: e.target.value}))}
								/>
							</FormControl>
						</ModalBody>

						<ModalFooter>
							<Button colorScheme='blue' mr={3} type='submit' isLoading={isLoading}>
								Update
							</Button>
							<Button onClick={onClose}>Cancel</Button>
						</ModalFooter>
					</ModalContent>
				</form>
			</Modal>
		</>
	);
}

export default EditModal;