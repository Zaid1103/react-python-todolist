import { Button, Container, Stack, Text } from "@chakra-ui/react"
import Navbar from "./components/Navbar"
import UserGrid from "./components/UserGrid"
import { useState } from "react";

export const BASE_URL = "http://127.0.0.1:5000/api"

function App() {
  const [tasks, setTasks] = useState([]);

  return (
    <Stack minH={"100vh"}>
      <Navbar setTasks={setTasks} />
       
       <Container maxW={"1200px"} my={4}>
          <Text
            fontSize={{ base: "3x1", md: "50" }}
            fontWeight={"bold"}
            letterSpacing={"2px"}
            textTransform={"uppercase"}
            textAlign={"center"}
            mb={8}
          >
            <Text 
              as={"span"}
              bgGradient={"linear(to-r, cyan.400, blue.500)"} bgClip={"text"}
            >
              Tasks to Complete
            </Text>

          </Text>

          <UserGrid tasks={tasks} setTasks={setTasks} />
       </Container>

    </Stack>
    
  )
}

export default App
