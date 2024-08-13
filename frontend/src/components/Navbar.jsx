import { Box, Button, Container, Flex, useColorMode, Text, useColorModeValue } from '@chakra-ui/react';
import React from 'react'
import { IoMoon } from 'react-icons/io5';
import { LuSun } from 'react-icons/lu';
import CreateUserModal from './CreateUserModal';


const Navbar = ({setTasks}) => {
  // may need to import icons if not working
  const { colorMode, toggleColorMode } = useColorMode()
  return (
    <Container maxW={"900px"}>
      <Box
      px={4}
      my={4}
      borderRadius={5}
      bg={useColorModeValue("gray.200", "gray.700")}
      >
        <Flex h="16"
        alignItems={"center"}
        justifyContent={"space-between"}>
          {/* left side */}
          <Flex
           alignItems={"center"}
           justifyContent={"center"}
           gap={3}
           display={{base: "none", sm:"flex"}}
           >
            <img src="/checkoff.png" alt="checkoff logo" width={60} height={60} />

           </Flex>
          {/* right side */}
          <Flex 
          gap={3}
          alignItems={"center"}>
            <Text fontSize={"lg"} fontWeight={500} display={{base: "none", md: "block"}}>
              Tasks
            </Text>
            <Button onClick={toggleColorMode}>
              {colorMode === "light" ? <IoMoon /> : <LuSun size={20} />  }
            </Button>
            <CreateUserModal setTasks={setTasks} />
          </Flex>
        </Flex>
      </Box>
      
    </Container>
  )
}

export default Navbar